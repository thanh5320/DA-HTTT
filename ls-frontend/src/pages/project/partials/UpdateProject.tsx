import Button from "components/button/button";
import Icon from "components/icon";
import React from "react";
import { FormProvider, useForm } from "react-hook-form";
import { useMutation } from "react-query";
import { Modal } from "reactstrap";
import { ProjectService } from "services/projectService";
import Common from "utils/common";
import ProjectForm from "./ProjectForm";

const UpdateProject = ({ modal, toggle, item }) => {
  const methods = useForm({
    defaultValues: {
      name: item.name,
      rules: item.rules,
    },
  });

  const update = useMutation(
    (data: any) => {
      return ProjectService.update({ ...data, id: item.id });
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          console.log(data.result);
          toggle(true);
          Common.showToast("Sửa bộ truy vấn thành công", "success");
        } else {
          Common.showToast("Sửa bộ truy vấn không thành công", "error");
        }
      },
      onError: () => {
        Common.showToast("Sửa bộ truy vấn không thành công", "error");
      }
    }
  );

  return (
    <Modal isOpen={modal} backdrop={true} scrollable={true} toggle={() => toggle(false)}>
      <div className="container-create-project">
        <header>
          <p>Sửa bộ truy vấn </p>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </header>
        <FormProvider {...methods}>
          <form onSubmit={(e) => e.preventDefault()} className="container-create-project__body" >
            <ProjectForm />
            <Button
              onClick={methods.handleSubmit((data: any) => {
                update.mutateAsync(data);
              })}
              className="container-create-project__submit"
              type="submit"
            >
              Hoàn thành
            </Button>
          </form>
        </FormProvider>
      </div>
    </Modal>
  );
};

export default UpdateProject;
