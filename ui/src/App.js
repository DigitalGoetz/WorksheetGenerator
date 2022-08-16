import React from "react";
import Content from "./components/Content";
import Header from "./components/Header";
import './App.css';
import '@astrouxds/astro-web-components/dist/astro-web-components/astro-web-components.css'

const App = () => {

  return (
    <div className="main">
      <Header />
      <Content />

    </div>
  )
}

export default App