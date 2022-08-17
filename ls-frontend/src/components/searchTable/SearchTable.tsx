import Icon from "components/icon";
import Input from "components/input/input";
import Tags from "components/tags/Tags";
import React, { useEffect, useState, useRef } from "react";
import "./searchTable.scss";
import { IOption } from "model/OtherModel";
import Popover from "components/popover/popover";
import Radio from "components/radio/radio";
import SelectCustom from "components/selectCustom/selectCustom";
import Checkbox from "components/checkbox/checkbox";
import Button from "components/button/button";
import DoubleDatePicker from "components/DoubleDatePicker/DoubleDatePicker";

export interface ISearchTable {
  title: string;
  type: "select" | "checkbox" | "date";
  options?: IOption[];
  date?: [];
}

export interface IActionForSort {
  label: string;
  value: string;
}

export interface ISearchTableProps {
  list?: ISearchTable[];
  onChangeTable?: (data) => void;
  actions?: IActionForSort[];
  isSort?: boolean;
  sortDefault?: string;
  className?: string;
}

export interface ISearchItemProps {
  className?: string;
  label: string;
  type: string; //"select" | "checkbox";
  options: IOption[];
  callback?: (label, value) => void;
  data?: any;
  isShow?: boolean;
  date?: any;
  onChangeDate?: (start, end) => void;
}

const SearchItem = (props: ISearchItemProps) => {
  const { label, type, options, callback, className, data, isShow, onChangeDate, date } = props;
  const [isActive, setIsActive] = useState(isShow || false);
  const [selected, setSelected] = useState([]);
  const refActions = useRef(null);

  useEffect(() => {
    setSelected(data.filter((el) => el.label === label).map((el) => el.selected));
  }, [data]);

  useEffect(() => {
    function handleClickOutside(event) {
      if (isActive && refActions.current && !refActions.current.contains(event.target)) {
        setIsActive(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isActive]);
  return (
    <div className={`search-item ${className ? className : ""} `} ref={refActions}>
      <p
        onClick={() => {
          setIsActive(!isActive);
        }}
      >
        <span>{label}</span> <Icon className="icon-caretdown" name="CaretDown" />
      </p>
      {isActive ? (
        <Popover position="left" className="popover-searchtable" isTriangle={true}>
          {type === "select" ? (
            <SelectCustom
              value={selected[0]?.value}
              placeholder={`Chọn ${label}`}
              onChange={(e) => {
                setSelected([{ label: e.label, value: e.value }]);
              }}
              options={options}
              className="popover-select"
            />
          ) : null}
          {type === "checkbox" ? (
            <div className="popover-checkbox">
              {options.map((option, index) => (
                <Checkbox
                  checked={selected.map((el) => el.value).includes(option?.value) ? true : false}
                  key={index}
                  value={String(option.value)}
                  label={String(option.label)}
                  icon={<Icon name="Checked" />}
                  onChange={(e) => {
                    if (e.target.checked) {
                      setSelected([...selected, { label: option.label, value: e.target.value }]);
                    } else {
                      setSelected(selected.filter((el) => el.value !== e.target.value));
                    }
                  }}
                />
              ))}
            </div>
          ) : null}
          {type === "select" || type === "checkbox" ? (
            <Button
              color="primary"
              onClick={() => {
                callback(label, selected);
                setIsActive(!isActive);
              }}
            >
              Lọc
            </Button>
          ) : null}

          {type === "date" ? (
            <DoubleDatePicker value={date} toggle={() => setIsActive(!isActive)} callback={onChangeDate} />
          ) : null}
        </Popover>
      ) : null}
    </div>
  );
};


const SearchTable = (props: ISearchTableProps) => {
  const { list, onChangeTable, actions, isSort, sortDefault, className } = props;
  const [search, setSearch] = useState("");
  const [tags, setTags] = useState([]);
  const [isShowSort, setIsShowSort] = useState(false);
  const [sort, setSort] = useState(",");
  const refActions = useRef(null);
  const [date, setDate] = useState([]);

  useEffect(() => {
    function handleClickOutside(event) {
      if (isShowSort && refActions.current && !refActions.current.contains(event.target)) {
        setIsShowSort(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isShowSort]);

  useEffect(() => {
    onChangeTable({ tags, search, sort, date });
  }, [tags, search, sort, date]);

  return (
    <div className={`base-search-table ${className ? className : ""} `}>
      <div className="search-table">
        {list?.map((el, i) => (
          <SearchItem
            key={i}
            label={el.title}
            type={el.type}
            options={el.options}
            data={tags}
            date={date}
            callback={(label, selected) => {
              const tmp = selected.map((el) => {
                return { selected: el, label };
              });
              setTags([...tags.filter((el) => el.label !== label), ...tmp]);
            }}
            onChangeDate={(start, end) => {
              setDate([start, end]);
            }}
          />
        ))}
        <form
          className="search-item-form"
          onSubmit={(e) => {
            e.preventDefault();
            onChangeTable({ tags, search });
          }}
        >
          <Input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            icon={<Icon name="Search" className="search-item-input__icon" />}
            placeholder="Nhập nội dung tìm kiếm"
            className="search-item-input"
            error={search?.trim().length > 200 ? true : false}
            message={search?.trim().length > 200 ? "Không được nhập quá 200 kí tự" : null}
          />
        </form>

        {isSort ? (
          <button className="btn-short" ref={refActions}>
            <Icon name="Sortby" onClick={() => setIsShowSort(!isShowSort)} />
            {isShowSort && actions ? (
              <Popover position="right" className="popover-search">
                {actions.map((el, index) => (
                  <Radio
                    defaultChecked={el.value === sortDefault}
                    key={index}
                    value={el.value}
                    name="sort"
                    label={el.label}
                    onChange={(e) => setSort(e.target.value)}
                  />
                ))}
              </Popover>
            ) : null}
          </button>
        ) : null}
      </div>
      <div className="tags">
        {tags.map((t, index) => (
          <Tags key={index} onClick={() => setTags(tags.filter((el) => el !== t))}>
            {t.label + " " + t.selected.label}
          </Tags>
        ))}
      </div>
    </div>
  );
};

export default SearchTable;
