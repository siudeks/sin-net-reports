import React from "react";

export interface NotFoundProps {
    ignored: Boolean
}

export const NotFound: React.FC<NotFoundProps> = (props) => {

    return (
        <h1>NotFound</h1>
    )
}
