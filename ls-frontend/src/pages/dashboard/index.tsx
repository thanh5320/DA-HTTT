import Button from "components/button/button";
import React, { useState } from "react";
import { useMutation, useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import { DashBoardService } from "services/dashboardService";
import CreateDashboard from "./partials/CreateDashboard";
import "./index.scss";
import Dialog from "components/dialog/dialog";
import Common from "utils/common";
import UpdateDashboard from "./partials/UpdateDashboard";
import BoxTable from "components/boxTable/boxTable";
import Icon from "components/icon";

const dataFormat = ["col-s-break-spaces text-left", "text-left col-md"];
const titles = ["Mã dashboard", "Tên dashboard"];

const DashBoard = () => {
  const navigate = useNavigate();

  const [modalCreate, setModalCreate] = useState(false);

  const { data, refetch } = useQuery("fetchDashboad", DashBoardService.all, {
    onSuccess: (data) => {
      //
    },
  });
  const [modalDeleteDashboard, setModalDeleteDashboard] = useState(false);
  const [modalUpdate, setModalUpdate] = useState(false);
  const [itemSelected, setItemSelected] = useState(null);

  const deteleMutation = useMutation(
    (id) => {
      return DashBoardService.delete(id);
    },
    {
      onSuccess: (data) => {
        if (data.code === 0) {
          Common.showToast("Xoa dashboard thanh cong", "success");
        } else Common.showToast("Xoa dashboard ko thanh cong", "error");
      },
    }
  );

  const actions = [
    {
      title: "sửa",
      callback: (item) => {
        setItemSelected(item.raw);
        setModalUpdate(!modalUpdate);
      },
      icon: <Icon name="Edit" />,
      color: "#2F80ED",
    },
    {
      title: "xoá",
      callback: (item) => {
        setItemSelected(item.raw);
        setModalDeleteDashboard(!modalDeleteDashboard);
        //
      },
      icon: <Icon name="Delete" />,
      color: "#EB5757",
    },
  ];

  const dataMappingArray = (item) => [item.id, item.name];

  return (
    <div className="container-dashboard">
      {modalCreate && (
        <CreateDashboard
          modal={modalCreate}
          toggle={(d) => {
            if (d) {
              refetch();
            }
            setModalCreate(!modalCreate);
          }}
        />
      )}

      {modalUpdate && itemSelected && (
        <UpdateDashboard
          modal={modalUpdate}
          data={itemSelected}
          toggle={(d) => {
            if (d) {
              refetch();
            }
            setModalUpdate(!modalUpdate);
          }}
        />
      )}

      {modalDeleteDashboard && itemSelected && (
        <Dialog
          modal={modalDeleteDashboard}
          title="Xác nhận xóa dashboard"
          message={`Bạn có chắn chắn muốn xóa dashboard ${itemSelected.name}`}
          toggle={(d) => {
            if (d) {
              deteleMutation.mutate(itemSelected.id);
              refetch();
            }
            setModalDeleteDashboard(!modalDeleteDashboard);
          }}
        />
      )}
      <div className="dashboard-header">
        <h5>Tổng quan</h5>
        <Button color="primary" onClick={() => setModalCreate(!modalCreate)}>
          + Thêm dashboard
        </Button>
      </div>

      <BoxTable
        name="dashboard"
        titles={titles}
        dataFormat={dataFormat}
        actions={actions}
        dataMappingArray={dataMappingArray}
        items={data?.result}
        onClickRow={(item: any) => {
          navigate(`/dashboard/${item.id}`);
        }}
      />
    </div>
  );
};

export default DashBoard;
