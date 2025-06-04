import React from "react";
import Slider from "rc-slider";
import "rc-slider/assets/index.css";

export function InputSlider({ value, setValue, min, max }) {
  return (
    <div className="mx-3">
      <div className="flex justify-between text-sm mb-1">
        <span>{value[0]}</span>
        <span>{value[1]}</span>
      </div>
      <Slider
        range
        min={min}
        max={max}
        allowCross={false}
        value={value}
        onChange={setValue}
      />
    </div>
  );
}
