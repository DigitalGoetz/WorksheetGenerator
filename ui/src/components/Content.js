import React from "react";
import { useState, useEffect } from "react";
import './Content.css';
import axios from 'axios';
import { RuxButton, RuxRadioGroup, RuxRadio, RuxSlider } from '@astrouxds/react'

const Content = () => {
    const [operation, setOperation] = useState("addition");
    const [digits, setDigits] = useState(3);
    const [genUrl, setGenUrl] = useState(process.env.REACT_APP_API + "/addition/2");

    const generateWorksheet = () => {
        console.log("REQ: " + genUrl)
        axios({
            url: genUrl,
            method: 'GET',
            responseType: 'blob',
        }).then((response) => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            let filename = "Worksheet_" + operation + "_" + digits + ".pdf"
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
        });
    }

    const getOperation = (e) => {
        let radios = document.getElementById("opSelection");
        setOperation(radios.value);
    }

    const getSliderValue = () => {
        let slider = document.getElementById("digitCount");
        setDigits(slider.value);
    }

    useEffect(() => {
        setGenUrl(process.env.REACT_APP_API + "/" + operation + "/" + digits)
    }, [operation, digits]);

    return (
        <div className="content">

            <div>
                <RuxRadioGroup onRuxchange={getOperation} id="opSelection" name="radios" label="Radio group" className="content-spacing">
                    <RuxRadio value="addition" name="radios">Addition</RuxRadio>
                    <RuxRadio value="subtraction" name="radios">Subtraction</RuxRadio>
                </RuxRadioGroup>
            </div>
            <div>
                <RuxSlider onRuxchange={getSliderValue} value={digits} id="digitCount" max="5" min="1" step="1" ticks-only={false} axisLabels={[1, 2, 3, 4, 5]} label="Maximum digits in each Number" className="content-spacing"></RuxSlider>
            </div>
            <div><RuxButton onClick={generateWorksheet}>Create Worksheet!</RuxButton></div>
        </div>

    )
}

export default Content