import Icon from "components/icon";
import React from "react";
import "./breadcrumbs.scss";

export interface IBreadcrumbItemProps {
  className?: string;
  children?: any;
  active?: boolean;
  onClick?: any;
  iconRight?: any;
}

const BreadcrumbItem = (props: IBreadcrumbItemProps) => {
  const { className, children, active, onClick, iconRight } = props;
  return (
    <div
      className={`base-breadcrumb-item ${className ? className : ""} ${active ? "breadcrumb-active" : ""} `}
      onClick={onClick}
    >
      {children}
      {iconRight ? iconRight : <Icon name="ChevronRight" />}
    </div>
  );
};

export default BreadcrumbItem;
