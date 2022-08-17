import Input from "components/input/input";
import SelectCustom from "components/selectCustom/selectCustom";
import React, { useState } from "react";
import { Controller, useForm, useFormContext, useFormState } from "react-hook-form";
import { useQuery } from "react-query";
import Select from "react-select";
import { ChartService } from "services/chartService";
import { ProjectService } from "services/projectService";

const ChartForm = () => {
  const { register, control } = useFormContext();

  const [type, setType] = useState([]);
  const [category, setCategory] = useState([]);
  //bo truy van
  const [projects, setProjects] = useState([]);

  const typeQuery = useQuery(["fetchGetType"], ChartService.getType, {
    onSuccess: (data) => {
      if (data.code === 0) {
        setType(data.result.map((el) => ({ value: el.id, label: el.name })));
      }
    },
  });
  const categoryQuery = useQuery(["fetchGetCategory"], ChartService.getCategory, {
    onSuccess: (data) => {
      if (data.code === 0) {
        setCategory(data.result.map((el) => ({ value: el.id, label: el.name })));
      }
    },
  });

  const projectQuery = useQuery(["fetchProject"], ProjectService.all, {
    onSuccess: (data) => {
      if (data.code === 0) {
        setProjects(data.result.map((el) => ({ value: el.id, label: el.name })));
      }
    },
  });
  if (typeQuery.isLoading || projectQuery.isLoading || categoryQuery.isLoading) return <p>loading...</p>;
  return (
    <div className="container-chart-form">
      <Input
        className="container-chart-form__field"
        name="name"
        type="text"
        label="Tên biểu đồ"
        placeholder="Nhập tên biểu đồ"
        register={register("name")}
      />
      <Controller
        name="chart_type_id"
        control={control}
        render={({ field: { onChange, value } }) => (
          <SelectCustom
            className="container-chart-form__field"
            //defaultValue={type.find((el) => el.value === value)}
            value={value}
            label="Kiểu biều đồ"
            placeholder="Chọn kiểu biểu đồ"
            options={type}
            onChange={(e: any) => {
              console.log(value);
              onChange(e.value);
            }}
          />
        )}
      />
      <Controller
        name="chart_category_id"
        control={control}
        render={({ field: { onChange, value } }) => (
          <SelectCustom
            className="container-chart-form__field"
            label="Loại biểu đồ"
            value={value}
            placeholder="Chọn loại biều đồ"
            options={category}
            onChange={(e: any) => onChange(e.value)}
          />
        )}
      />
      <div className="container-chart-form__field">
        <label htmlFor="">Bộ truy vấn</label>
        <Controller
          name="project_ids"
          control={control}
          render={({ field: { onChange, value } }) => (
            <Select
              value={projects.filter((el) => {
                if (value.includes(el.value)) {
                  return { value: el.value, label: el.label };
                }
              })}
              placeholder="Chọn bộ truy vấn"
              isMulti
              options={projects}
              onChange={(options) => {
                onChange(options.map((el: any) => el.value));
              }}
            />
          )}
        />
      </div>
    </div>
  );
};

export default ChartForm;
