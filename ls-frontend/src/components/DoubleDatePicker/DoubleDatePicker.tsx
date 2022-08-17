import React, { useState } from "react";
import DatePicker from "react-datepicker";
import Icon from "components/icon";
import moment from "moment";
import "react-datepicker/dist/react-datepicker.css";
import "./DoubleDatePicker.scss";
import Button from "components/button/button";

interface IRangeDatePickerProps {
  callback: any;
  toggle?: any;
  value?: any;
  className?: any;
}

const DoubleDatePicker = (props: IRangeDatePickerProps) => {
  const { callback, toggle, value, className } = props;
  const [startDate, setStartDate] = useState(value[0] ? moment(value[0]).toDate() : moment().toDate());
  const [endDate, setEndDate] = useState(value[1] ? moment(value[1]).toDate() : null);
  const onChange = (dates) => {
    const [start, end] = dates;
    setStartDate(start);
    setEndDate(end);
    //callback(start, end);
  };
  //const [active, setActive] = useState()
  const days = ["CN", "T2", "T3", "T4", "T5", "T6", "T7"];
  const months = [
    "Tháng 1",
    "Tháng 2",
    "Tháng 3",
    "Tháng 4",
    "Tháng 5",
    "Tháng 6",
    "Tháng 7",
    "Tháng 8",
    "Tháng 9",
    "Tháng 10",
    "Tháng 11",
    "Tháng 12",
  ];

  const locale = {
    localize: {
      day: (n) => days[n],
      month: (n) => months[n],
    },
    formatLong: {
      date: () => "mm/dd/yyyy",
    },
  };
  return (
    <div className={`base-doubledatepicker ${className ? className : ''}`}>
      <div className="base-doubledatepicker__content">
        <ul className="menu">
          <li
            onClick={() => {
              setStartDate(moment().toDate());
              setEndDate(moment().toDate());
            }}
          >
            Hôm nay
          </li>
          <li
            onClick={() => {
              setStartDate(moment().add(-1, "day").toDate());
              setEndDate(moment().add(-1, "day").toDate());
            }}
          >
            Hôm qua
          </li>
          <li
            onClick={() => {
              setStartDate(moment().add(-7, "day").toDate());
              setEndDate(moment().toDate());
            }}
          >
            Tuần này
          </li>
          <li
            onClick={() => {
              setStartDate(moment().add(-14, "day").toDate());
              setEndDate(moment().toDate());
            }}
          >
            14 ngày gần đây
          </li>
          <li
            onClick={() => {
              setStartDate(moment().add(-30, "day").toDate());
              setEndDate(moment().toDate());
            }}
          >
            30 ngày gần đây
          </li>
          <li
            onClick={() => {
              setStartDate(moment().date(1).toDate());
              setEndDate(moment().toDate());
            }}
          >
            Tháng nay
          </li>
        </ul>
        <DatePicker
          selected={startDate}
          onChange={onChange}
          startDate={startDate}
          endDate={endDate}
          selectsRange
          inline
          locale={locale}
          calendarStartDay={1}
          monthsShown={2}
        />
      </div>
      <div className="base-doubledatepicker__footer">
        <p>
          Từ {moment(startDate).format("DD/MM/YYYY")} đến {endDate ? moment(endDate).format("DD/MM/YYYY") : null}
        </p>
        <div className="base-doubledatepicker__footer-btns">
          <Button
            color="transparent"
            onClick={() => {
              toggle();
              //callback(startDate, endDate);
            }}
          >
            Hủy
          </Button>
          <Button
            onClick={() => {
              callback(startDate, endDate);
              toggle();
            }}
          >
            Áp dụng
          </Button>
        </div>
      </div>
    </div>
  );
};

export default DoubleDatePicker;
