import { Stack, TextField } from "@fluentui/react";
import React, { useState } from "react";

interface PeriodSelectorProps {
    suffix: string,
    year?: number,
    month?: number,
    onYearChanged: (value?: number) => void,
    onMonthChanged: (value?: number) => void,
}


export const PeriodSelector: React.FC<PeriodSelectorProps> = props => {

    const yearRe = /^(19|20)\d{2}$/;
    const monthRe = /^(0?[1-9]|1[012])$/;
  
    const onChangeYear = (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
      setYearText(newValue);
  
      const newValueAsString = newValue ?? "";
      const newValueAsInt = yearRe.test(newValueAsString) ? parseInt(newValueAsString) : undefined;
      if (newValueAsInt === props.year) return;
      props.onYearChanged(newValueAsInt);
    }
  
    const onChangeMonth = (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
      setMonthText(newValue);
  
      const newValueAsString = newValue ?? "";
      const newValueAsInt = monthRe.test(newValueAsString) ? parseInt(newValueAsString) : undefined;
      if (newValueAsInt === props.month) return;
      props.onMonthChanged(newValueAsInt);
    }
  
    const [yearText, setYearText] = useState<string | undefined>((props.year ?? '').toString());
    const [monthText, setMonthText] = useState<string | undefined>((props.month ?? '').toString());
  
    return (<Stack horizontal>
        <Stack.Item>
            <TextField label={`Rok ${props.suffix}`} value={yearText} onChange={onChangeYear} />
        </Stack.Item>
        <Stack.Item>
            <TextField label={`Miesiąc ${props.suffix}`} value={monthText} onChange={onChangeMonth} />
        </Stack.Item>
    </Stack>);

}