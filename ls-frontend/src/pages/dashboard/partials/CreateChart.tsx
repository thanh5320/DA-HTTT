import React, { useState } from "react";
import { useMutation, useQuery } from "react-query";
import { Modal } from "reactstrap";
import { ChartService } from "services/chartService";
import Select from "react-select";
import Button from "components/button/button";
import { Controller, FormProvider, useForm } from "react-hook-form";
import { ProjectService } from "services/projectService";
import Input from "components/input/input";
import Common from "utils/common";
import SelectCustom from "components/selectCustom/selectCustom";
import Icon from "components/icon";
import ChartForm from "./ChartForm";

const CreateChart = ({ modal, toggle, dashboard_id }) => {
  const [type, setType] = useState([]);
  const [category, setCategory] = useState([]);
  //bo truy van
  const [projects, setProjects] = useState([]);

  const methods = useForm({
    defaultValues: {
      name: "",
      chart_type_id: null,
      chart_category_id: null,
      project_ids: [],
      id: dashboard_id,
    },
  });

  const createMutation = useMutation(
    (data: any) => {
      return ChartService.create(data.name, data.chart_type_id, data.chart_category_id, dashboard_id, data.project_ids);
    },
    {
      onSuccess: (data) => {
        console.log(data);
        if (data.code === 0) {
          Common.showToast("Thêm biểu đồ thành công", "success");
          toggle(true);
        } else {
          Common.showToast("Thêm biểu đồ thất bại", "error");
        }
      },
      onError: () => {
        Common.showToast("Thêm biểu đồ thất bại", "error");
      }
    }
  );
  return (
    <Modal isOpen={modal} backdrop={true} style={{ height: "80vh" }} scrollable={true} toggle={() => toggle(false)}>
      <div className="update-chart">
        <header>
          <p>Tạo biều đồ</p>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </header>
        <FormProvider {...methods}>
          <form
            onSubmit={methods.handleSubmit((data) => {
              console.log(data);
              createMutation.mutateAsync(data);
            })}
          >
            <ChartForm />
            <Button type="submit">Hoàn thành</Button>
          </form>
        </FormProvider>
      </div>
    </Modal>
  );
};

export default CreateChart;
