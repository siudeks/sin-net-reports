import React from "react";

interface DurationProps {
    duration: number | undefined
}

export const Duration: React.FC<DurationProps> = props => {

    const duration = props.duration ?? 0;
    var hours = Math.floor(duration / 60);
    var minutes = duration - hours * 60;
    var minutesAsText = ('00' + minutes).substr(-2);
    const durationAsText = `${hours}:${minutesAsText}`;
    return (<>
        {durationAsText}
    </>);

}
