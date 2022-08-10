import React from "react";
import './Content.css';
import { RuxButton, RuxRadioGroup, RuxRadio, RuxSlider } from '@astrouxds/react'

const Content = () => {
    return (
        <div className="content">

            <div>
                <RuxRadioGroup name="radios" label="Radio group" className="content-spacing">
                    <RuxRadio value="addition" name="radios">Addition</RuxRadio>
                    <RuxRadio value="subtraction" name="radios">Subtraction</RuxRadio>
                </RuxRadioGroup>
            </div>
            <div>
                <RuxSlider id="axis-labels" max="5" min="1" step="1" ticks-only={false} axisLabels={[1, 2, 3, 4, 5]} label="Maximum digits in each Number" className="content-spacing"></RuxSlider>
            </div>
            <div><RuxButton>Create Worksheet!</RuxButton></div>
        </div>

    )
}

export default Content