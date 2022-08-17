import Button from "components/button/button";
import Icon from "components/icon";
import React from "react";
import { Modal } from "reactstrap";
import "./dialog.scss";
import BeatLoader from "react-spinners/BeatLoader";

// interface DialogProps {
//   color: "success" | "error";
//   className: string;
//   title: any;
//   message: any;
//   cancelText?: string;
//   cancelAction?: () => void;
//   defaultText: string;
//   defaultAction: () => void;
// }

// export default function Dialog(props: DialogProps) {
//   const { color, className, title, message, cancelText, cancelAction, defaultText, defaultAction } = props;

//   const [isLoading, setIsLoading] = useState<boolean>(false);

//   return (
//     <>
//       <div className={`base-dialog${color ? " base-dialog--color-" + color : ""}${className ? " " + className : ""}`}>
//         <h3 className="d-flex align-items-center">
//           {color === "success" ? <Icon name="check-circle" /> : color === "error" ? <Icon name="times-circle" /> : null}
//           {title}
//         </h3>
//         <div className="base-dialog__content">{message}</div>
//         <div className="base-dialog__actions">
//           {cancelText && cancelAction !== null ? (
//             <Button type="button" className="btn-cancel" color="primary" variant="outline" onClick={() => cancelAction()}>
//               {cancelText}
//             </Button>
//           ) : null}
//           <Button
//             type="button"
//             className="btn-default"
//             disabled={isLoading}
//             color={color === "success" ? "primary" : "destroy"}
//             onClick={() => {
//               defaultAction();
//               setIsLoading(true);
//             }}
//           >
//             {defaultText}
//             {isLoading ? <Icon name="spinner" /> : null}
//           </Button>
//         </div>
//       </div>
//       <div className="base-dialog__overlay"></div>
//     </>
//   );
// }

export interface IDialogProps {
  message: string;
  modal: boolean;
  toggle: (d) => any;
  title: string;
  isLoading?: boolean;
}

const Dialog = (props: IDialogProps) => {
  const { message, modal, toggle, title, isLoading } = props;
  return (
    <Modal isOpen={modal} backdrop={true} scrollable={true} centered toggle={() => toggle(false)}>
      <div className="base-dialog">
        <div className="base-dialog__header base-dialog__header-delete">
          <p>{title}</p>
          <Icon name="Xmark" onClick={() => toggle(false)} />
        </div>
        <p className="base-dialog__body">{message}?</p>
        <div className="base-dialog__footer">
          <Button
            variant="outline"
            onClick={() => {
              toggle(false);
            }}
            disabled={isLoading}
          >
            Huỷ
          </Button>
          <Button
            onClick={() => {
              toggle(true);
            }}
            disabled={isLoading}
          >
            {isLoading ? <BeatLoader color={"#E5E5E5"} loading={true} size={5} /> : null}
            Đồng ý
          </Button>
        </div>
      </div>
    </Modal>
  );
};

export default Dialog;
