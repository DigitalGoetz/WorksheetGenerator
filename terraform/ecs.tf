resource "aws_ecs_service" "worksheet-api" {
  name            = "worksheet-api"
  task_definition = aws_ecs_task_definition.worksheet-api-task-def.arn
  launch_type     = "FARGATE"
  cluster         = aws_ecs_cluster.worksheet-cluster.id
  desired_count   = 1

  network_configuration {
    assign_public_ip = false

    security_groups = [
      aws_security_group.egress_all.id, aws_security_group.ingress_api.id
    ]

    subnets = [
      aws_subnet.private_d.id, aws_subnet.private_e.id
    ]
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.worksheet-lb.arn
    container_name   = "worksheet-api"
    container_port   = "4222"
  }
}

resource "aws_lb_target_group" "worksheet-lb" {
  name        = "worksheet-lb"
  port        = 4222
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.worksheet-vpc.id

  health_check {
    enabled = true
    path    = "/health"
  }

  depends_on = [
    aws_alb.worksheet-alb
  ]
}

resource "aws_alb" "worksheet-alb" {
  name               = "worksheet-api-alb"
  internal           = false
  load_balancer_type = "application"
  subnets = [
    aws_subnet.public_d.id,
    aws_subnet.public_e.id,
  ]

  security_groups = [
    aws_security_group.http.id,
    aws_security_group.https.id,
    aws_security_group.egress_all.id,
  ]

  depends_on = [aws_internet_gateway.igw]
}

resource "aws_alb_listener" "worksheet-api-http" {
  load_balancer_arn = aws_alb.worksheet-alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.worksheet-lb.arn
  }
}

resource "aws_cloudwatch_log_group" "worksheet-api-logs" {
  name = "/ecs/worksheet-api"
}

resource "aws_ecs_task_definition" "worksheet-api-task-def" {
  family = "worksheet-apis"

  execution_role_arn    = aws_iam_role.worksheet_api_task_execution_role.arn
  container_definitions = <<EOF
  [
    {
        "name": "worksheet-api",
        "image": "public.ecr.aws/a5z6g2x8/simple-worksheet:1.0.1",
        "portMappings": [
            {
                "containerPort": 4222
            }
        ],
        "logConfiguration": {
            "logDriver": "awslogs",
            "options": {
                "awslogs-region": "us-east-2",
                "awslogs-group": "/ecs/worksheet-api",
                "awslogs-stream-prefix": "ecs"
            }
        }
    }
  ]
  EOF

  cpu                      = 256
  memory                   = 512
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
}

resource "aws_iam_role" "worksheet_api_task_execution_role" {
  name               = "worksheet-api-task-execution-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

data "aws_iam_policy_document" "ecs_task_assume_role" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

data "aws_iam_policy" "ecs_task_execution_role" {
  arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role" {
  role       = aws_iam_role.worksheet_api_task_execution_role.name
  policy_arn = data.aws_iam_policy.ecs_task_execution_role.arn
}

resource "aws_ecs_cluster" "worksheet-cluster" {
  name = "worksheet-cluster"
}
