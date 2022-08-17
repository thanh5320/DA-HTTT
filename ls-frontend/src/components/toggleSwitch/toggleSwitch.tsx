import React from "react";
import "./toggleSwitch.scss";

export default function ToggleSwitch() {
  return (
    <>
      <label className="switch">
        <input type="checkbox" />
        <span className="slider round"></span>
      </label>
    </>
  );
}
