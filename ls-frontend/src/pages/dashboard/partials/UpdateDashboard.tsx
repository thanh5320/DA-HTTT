import Button from "components/button/button";
import Icon from "components/icon";
import Input from "components/input/input";
import React from "react";
import { useForm } from "react-hook-form";
import { useMutation } from "react-query";
import { Modal } from "reactstrap";
import { DashBoardService } from "services/dashboardService";
import Common from "utils/common";

const UpdateDashboard = ({ data, modal, toggle }) => {
  const { register, handleSubmit } = useForm({
    defaultValues: {
      name: data.name,
    },
  });

  const updateMutation = useMutation(
    (data: any) => {
      return DashBoardService.update(data.id, data.name);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          toggle(true);
          Common.showToast("Sửa dashboard thành công", "success");
        } else {
          Common.showToast("Sửa dashboard không thành công", "error");
        }
      },
      onError: () => {
        Common.showToast("Sửa dashboard không thành công", "error");
      }
    }
  );
  return (
    <Modal isOpen={modal} backdrop={true} scrollable={true} toggle={() => toggle(false)}>
      <div className="container-create-dashboard">
        <header>
          <h5>Sửa dashboard</h5>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </header>
        <form
          onSubmit={handleSubmit((t: any) => {
            updateMutation.mutateAsync({ name: t.name, id: data.id });
          })}
        >
          <Input label="Tên dashboad" placeholder="Nhập tên dashboard" register={register("name")} />
          <Button className="container-create-dashboard__btn" type="submit">
            Hoàn thành
          </Button>
        </form>
      </div>
    </Modal>
  );
};

export default UpdateDashboard;
