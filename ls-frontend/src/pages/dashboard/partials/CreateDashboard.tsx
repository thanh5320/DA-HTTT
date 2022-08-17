import Button from "components/button/button";
import Icon from "components/icon";
import Input from "components/input/input";
import React from "react";
import { useForm } from "react-hook-form";
import { useMutation } from "react-query";
import { Modal } from "reactstrap";
import { DashBoardService } from "services/dashboardService";
import Common from "utils/common";

const CreateDashboard = ({ modal, toggle }) => {
  const { register, handleSubmit } = useForm();

  const createMutation = useMutation(
    (data) => {
      return DashBoardService.create(data);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          toggle(true);
          Common.showToast("Tạo dashboard thành công", "success");
        } else {
          Common.showToast("Tạo dashboard không thành công", "error");
        }
      },
      onError: () => {
        Common.showToast("Tạo dashboard không thành công", "error");
      }
    }
  );
  return (
    <Modal isOpen={modal} backdrop={true} scrollable={true} toggle={() => toggle(false)}>
      <div className="container-create-dashboard">
        <header>
          <h5>Tạo dashboard</h5>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </header>
        <form
          onSubmit={handleSubmit((t: any) => {
            createMutation.mutateAsync(t.name);
          })}
        >
          <Input label="Tên dashboad" placeholder="Nhập tên dashboard" register={register("name")} />
          <Button className="container-create-dashboard__btn" type="submit">
            Hoàn thành
          </Button>
        </form>
      </div>
      {/* <form
        onSubmit={handleSubmit((data: any) => {
          createMutation.mutateAsync(data.name);
        })}
      >
        <Input label="Ten dashboad" placeholder="Nhap ten dashboard" register={register("name")} />
        <Button>Tao dashboard</Button>
      </form> */}
    </Modal>
  );
};

export default CreateDashboard;
