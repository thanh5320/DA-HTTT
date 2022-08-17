import Button from "components/button/button";
import Icon from "components/icon";
import React from "react";
import { FormProvider, useForm } from "react-hook-form";
import { useMutation } from "react-query";
import { Modal } from "reactstrap";
import { ChartService } from "services/chartService";
import Common from "utils/common";
import ChartForm from "./partials/ChartForm";

const UpdateChart = ({ data, modal, toggle }) => {
  const methods = useForm({
    defaultValues: {
      name: data.name,
      chart_type_id: data.chart_type_id,
      chart_category_id: data.chart_category_id,
      project_ids: data.project_ids,
      id: data.id,
    },
  });

  const updateChartMutation = useMutation(
    (data: any) => {
      return ChartService.update(data.id, data.name, data.chart_type_id, data.chart_category_id, data.project_ids);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          console.log(data.result);
          Common.showToast("Cập nhật biểu đồ thành công", "success");
          toggle(true);
        } else {
          Common.showToast("Cập nhật biều đồ không thành công", "error");
        }
      },
      onError: () => {
        Common.showToast("Cập nhật biều đồ không thành công", "error");
      }
    }
  );

  return (
    <Modal isOpen={modal} backdrop={true} scrollable={true} toggle={() => toggle(false)}>
      <div className="update-chart">
        <header>
          <p>Sửa biều đồ</p>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </header>
        <FormProvider {...methods}>
          <form
            onSubmit={methods.handleSubmit((data) => {
              console.log(data);
              updateChartMutation.mutateAsync(data);
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

export default UpdateChart;
