import React from "react";
import Icon from "components/icon";
import { IRouter } from "model/MenuModel";
import Project from "pages/project";
import DashBoard from "pages/dashboard";
import Detail from "pages/dashboard/partials/Detail";
import News from "pages/new";

export const routes: IRouter[] = [
  {
    path: "/project",
    component: <Project />,
  },
  {
    path: "/",
    component: <DashBoard />,
  },
  {
    path: "/dashboard/:id",
    component: <Detail />,
  },
  {
    path: "/news",
    component: <News />,
  },
];
