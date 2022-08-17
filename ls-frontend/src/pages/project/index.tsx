import BoxTable from "components/boxTable/boxTable";
import Button from "components/button/button";
import Dialog from "components/dialog/dialog";
import Icon from "components/icon";
import Input from "components/input/input";
import React, { useState } from "react";
import { useMutation, useQuery } from "react-query";
import { Modal } from "reactstrap";
import { ProjectService } from "services/projectService";
import Common from "utils/common";
import "./index.scss";
import CreateProject from "./partials/CreateProject";
import UpdateProject from "./partials/UpdateProject";

const dataFormat = ["col-s-break-spaces text-left", "text-left col-md"];
const titles = ["Mã bộ truy vấn", "Tên bộ truy vấn"];

const Project = () => {
  const [modalCreate, setModalCreate] = useState(false);
  const [modalDelete, setModalDelete] = useState(false);
  const [modalUpdate, setModalUpdate] = useState(false);

  const [itemSelected, setItemSelected] = useState(null);

  const actions = [
    {
      title: "sửa",
      callback: (item) => {
        setItemSelected({
          id: item.raw.id,
          name: item.raw.name,
          rules: [
            ...item.raw.rules.map((rule) => ({
              main_keywords: rule.main_keywords.map((el) => el.value),
              sub_keywords: rule.sub_keywords.map((el) => el.value),
              exclude_keywords: rule.exclude_keywords.map((el) => el.value),
            })),
          ],
        });
        setModalUpdate(!modalUpdate);
      },
      icon: <Icon name="Edit" />,
      color: "#2F80ED",
    },
    {
      title: "xoá",
      callback: (item) => {
        setItemSelected(item.raw);
        setModalDelete(!modalDelete);
        //
      },
      icon: <Icon name="Delete" />,
      color: "#EB5757",
    },
  ];

  const dataMappingArray = (item) => [item.id, item.name];

  const { data, refetch } = useQuery("fetchProjects", ProjectService.all, {
    onSuccess: (data) => {
      console.log(data);
      if (data.code === 0) {
        console.log(data);
        //
      } else {
        Common.showToast("Vui lòng thử lại", "error");
      }
    },
  });

  const deleteMutation = useMutation(
    (id) => {
      return ProjectService.delete(id);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          Common.showToast("Xóa bộ truy vấn thành công", "success");
          refetch();
          setModalDelete(!modalDelete);
        }
        else {
          Common.showToast("Xóa bộ truy vấn không thành công", "error");
        }
      },
      onError: () => {
        Common.showToast("Xóa bộ truy vấn không thành công", "error");
      }
    }
  );
  return (
    <div className="container-project">
      {modalCreate ? (
        <CreateProject
          modal={modalCreate}
          toggle={(d) => {
            if (d) {
              refetch();
            }
            setModalCreate(!modalCreate);
          }}
        />
      ) : null}
      {modalDelete && itemSelected && (
        <Dialog
          modal={modalDelete}
          message={`Bạn chắc chắn muốn xoá bộ truy vấn ${itemSelected.name}`}
          title="Xác nhận xóa bộ truy vấn"
          isLoading={deleteMutation.isLoading}
          toggle={(d) => {
            //d === true tức là thực hiện thao tác xoá => đợi xoá thành công sau đó mới đóng popup
            if (d) deleteMutation.mutate(itemSelected.id);
            // ko xoá => đóng popup
            else setModalDelete(false);
          }}
        />
      )}
      {modalUpdate && itemSelected && (
        <UpdateProject
          modal={modalUpdate}
          toggle={(d) => {
            //
            if (d) {
              refetch();
            }
            setModalUpdate(!modalUpdate);
          }}
          item={itemSelected}
        />
      )}
      <div className="container-project__header">
        <h2>Danh sách bộ truy vấn</h2>
        <Button color="primary" onClick={() => setModalCreate(!modalCreate)}>
          + Thêm bộ truy vấn
        </Button>
      </div>

      <BoxTable
        name="bộ truy vấn"
        titles={titles}
        dataFormat={dataFormat}
        items={data?.result}
        actions={actions}
        dataMappingArray={dataMappingArray}
      />
    </div>
  );
};

export default Project;
