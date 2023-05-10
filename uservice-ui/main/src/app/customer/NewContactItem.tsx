import { DefaultButton, IStackTokens, Stack, TextField } from "@fluentui/react";
import React from "react";
import { ContactDetails } from "./CustomerView";

const stackTokens: IStackTokens = { childrenGap: 12 };

interface NewContactItemProps {
    model: ContactDetails,
    onChange: (newValue: ContactDetails) => void,
    onRemove: (localKey: string) => void

}

export const NewContactItem: React.FC<NewContactItemProps> = props => {
    const { localKey, firstName, lastName, phoneNo, email } = props.model;
    const handler = (action: (m: ContactDetails, v?: string) => void) => {
        return (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            var newModel: ContactDetails = {
                localKey,
                firstName,
                lastName,
                phoneNo,
                email
            };
            action(newModel, newValue);
            props.onChange(newModel);
        }
    }
    
    const extendedWidth = { minWidth: "300px" }

    return (<div className="ms-Grid-row">
        <div className="ms-Grid-col ms-smPush1">
            <Stack tokens={stackTokens}>
                <Stack horizontal tokens={stackTokens}>
                    <TextField style={extendedWidth} placeholder="Imię" value={firstName} onChange={handler((m, v) => m.firstName = v)} />
                    <TextField style={extendedWidth} placeholder="Nazwisko" value={lastName} onChange={handler((m, v) => m.lastName = v)} />
                    <TextField style={extendedWidth} placeholder="Nr telefonu" value={phoneNo} onChange={handler((m, v) => m.phoneNo = v)} />
                    <TextField style={extendedWidth} placeholder="email" value={email} onChange={handler((m, v) => m.email = v)} />
                    <DefaultButton text="Usuń" onClick={() => props.onRemove(localKey)} />
                </Stack>
            </Stack>
        </div>
    </div>);

}
