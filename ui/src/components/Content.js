import React from "react";
import { useState, useEffect } from "react";
import './Content.css';
import axios from 'axios';
import { RuxCheckbox, RuxButton, RuxRadioGroup, RuxRadio, RuxSlider } from '@astrouxds/react'

const Content = () => {
    const [operation, setOperation] = useState("addition");
    const [isSubtraction, setIsSubtraction] = useState(false);
    const [allowNegatives, setAllowNegatives] = useState(false);
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
        if (radios.value === "subtraction") {
            setIsSubtraction(true);
        } else {
            setIsSubtraction(false);
        }
        if (radios.value === "addition") {
            setAllowNegatives(false);
        }
    }

    const getSliderValue = () => {
        let slider = document.getElementById("digitCount");
        setDigits(slider.value);
    }

    const getAllowNegatives = () => {
        let checkbox = document.getElementById("rux-check")
        if (checkbox.checked) {
            setAllowNegatives(true);
        } else {
            setAllowNegatives(false);
        }
    }

    useEffect(() => {
        if (isSubtraction) {
            setGenUrl(process.env.REACT_APP_API + "/" + operation + "/" + digits + "?negatives=" + allowNegatives)
        } else {
            setGenUrl(process.env.REACT_APP_API + "/" + operation + "/" + digits)
        }
    }, [operation, digits, allowNegatives, isSubtraction]);

    return (
        <div className="content">

            <div>
                <RuxRadioGroup onRuxchange={getOperation} id="opSelection" name="radios" label="Radio group" className="content-spacing">
                    <RuxRadio value="addition" name="radios">Addition</RuxRadio>
                    <RuxRadio value="subtraction" name="radios">Subtraction</RuxRadio>
                </RuxRadioGroup>
                {isSubtraction ?
                    (<RuxCheckbox id="rux-check" onRuxchange={getAllowNegatives}>Allow Negative Results?</RuxCheckbox>) : (<div></div>)
                }
            </div>
            <div>
                <RuxSlider onRuxchange={getSliderValue} value={digits} id="digitCount" max="5" min="1" step="1" ticks-only={false} axisLabels={[1, 2, 3, 4, 5]} label="Maximum digits in each Number" className="content-spacing"></RuxSlider>
            </div>
            <div><RuxButton onClick={generateWorksheet}>Create Worksheet!</RuxButton></div>
        </div>

    )
}

export default Content