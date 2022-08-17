import Button from "components/button/button";
import Icon from "components/icon";
import React from "react";
import { FormProvider, useForm } from "react-hook-form";
import { useMutation } from "react-query";
import { Modal } from "reactstrap";
import { ProjectService } from "services/projectService";
import Common from "utils/common";
import ProjectForm from "./ProjectForm";

const CreateProject = ({ modal, toggle }) => {

  const methods = useForm({
    defaultValues: {
      name: "",
      rules: [],
    },
  });

  const create = useMutation(
    (data) => {
      return ProjectService.create(data);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          console.log(data.result);
          toggle(true);
          Common.showToast("Tạo bộ truy vấn thành công", "success");
        } else {
          Common.showToast("Tạo bộ truy vấn không thành công", "error");
        }
      },
      onError: () => {
        Common.showToast("Tạo bộ truy vấn không thành công", "error");
      }
    }
  );

  return (
    <Modal isOpen={modal} backdrop={true} scrollable={true} toggle={() => toggle(false)}>
      <div className="container-create-project">
        <header>
          <p>Tạo bộ truy vấn </p>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </header>
        <FormProvider {...methods}>
          <form onSubmit={(e) => e.preventDefault()} className="container-create-project__body">
            <ProjectForm />
            <Button
              onClick={methods.handleSubmit((data: any) => {
                create.mutateAsync(data);
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

export default CreateProject;
