import { DefaultButton, IStackTokens, Separator, Stack, TextField } from "@fluentui/react";
import React from "react";

const stackTokens: IStackTokens = { childrenGap: 12 };

interface UserPasswordModel {
    localKey: string,
    location: string
    username?: string,
    password?: string
}

interface UserPasswordItemProps {
    model: UserPasswordModel,
    changedBy: string,
    changedWhen: string,
    onChange: (newValue: UserPasswordModel) => void,
    onRemove: (localKey: string) => void
}

export const UserPasswordItem: React.FC<UserPasswordItemProps> = props => {
    const { localKey, location: sectionName, username, password } = props.model;
    const handler = (action: (m: UserPasswordModel, v?: string) => void) => {
        return (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            var newModel: UserPasswordModel = {
                localKey,
                location: sectionName,
                username,
                password
            };
            action(newModel, newValue);
            props.onChange(newModel);
        }
    }

    const extendedWidth = { minWidth: "300px" }

    return (
        <div className="ms-Grid-row">
            <div className="ms-Grid-col ms-smPush1">
                <Separator alignContent="start">{sectionName}</Separator>
                <Stack tokens={stackTokens}>
                    <Stack horizontal tokens={stackTokens}>
                        <TextField style={extendedWidth} placeholder="Użytkownik" value={username} onChange={handler((m, v) => m.username = v)} />
                        <TextField style={extendedWidth} placeholder="Hasło" value={password} onChange={handler((m, v) => m.password = v)} />
                        <DefaultButton text="Usuń" onClick={() => props.onRemove(props.model.localKey)} />
                    </Stack>
                    <Stack horizontal tokens={stackTokens}>
                        <TextField value={props.changedBy} disabled />
                        <TextField value={props.changedWhen} disabled/>
                    </Stack>
                </Stack>
            </div>
        </div>
    );
}


