import { DefaultButton, IStackTokens, Separator, Stack, TextField } from "@fluentui/react";
import React, { useEffect, useState } from "react";
import { TOTP } from "totp-generator"

const stackTokens: IStackTokens = { childrenGap: 12 };

interface UserPasswordExtModel {
    // internal ID used by React to identify the element on list of elements
    localKey: string,
    // editable properties of the component
    location: string
    username?: string,
    password?: string,
    entityName?: string,
    entityCode?: string
    otpSecret?: string
    otpRecoveryKeys?: string
}

interface UserPasswordExtItemProps {
    model: UserPasswordExtModel,
    changedBy: string,
    changedWhen: string,
    onChange: (newValue: UserPasswordExtModel) => void,
    onRemove: (localKey: string) => void
}



export const UserPasswordItemExt: React.FC<UserPasswordExtItemProps> = props => {
    const init = new Date()
    
    const [date, setDate] = useState(init)
        const tick = () => {
      setDate(new Date())
    }
    
    // refresh component every sec to allow recalculate totp
    // the logic should be moved to hook around TOTP returning code when changed
    useEffect(() => {
        const timerID = setInterval(() => tick(), 1000)
        return () => {
          clearInterval(timerID)
        }
      }, [])
      
    const { localKey, location, username, password, entityName, entityCode, otpSecret, otpRecoveryKeys } = props.model;
    const handler = (action: (m: UserPasswordExtModel, v?: string) => void) => {
        return (event: React.FormEvent<HTMLInputElement | HTMLTextAreaElement>, newValue?: string) => {
            var newModel: UserPasswordExtModel = {
                localKey,
                location,
                username,
                password,
                entityName,
                entityCode,
                otpSecret,
                otpRecoveryKeys
            };
            action(newModel, newValue);
            props.onChange(newModel);
        }
    }

    const extendedWidth1 = { minWidth: "300px" }
    const extendedWidth2 = { minWidth: "492px" }
    let otpDesc = "TOTP nie zdefiniowany"
    let expiresDesc = "-"
    const period = 30
    if (otpSecret) {
        try {
            const { otp, expires } = TOTP.generate(otpSecret!, {
                digits: 6,
                algorithm: "SHA-1",
                period: period
            })
            otpDesc = otp
            const now = new Date().getTime()
            const differenceInMillis = expires - now
            const differenceInSeconds = Math.floor(differenceInMillis / 1000);
            expiresDesc = differenceInSeconds + ""
        } catch (error) {
            otpDesc = "Nieprawidłowy sekret!"
            expiresDesc = "!"
        }
    }

    return (
        <div className="ms-Grid-row">
            <div className="ms-Grid-col ms-smPush1">
                <Separator alignContent="start">{props.model.location}</Separator>
                <Stack tokens={stackTokens}>
                    <Stack horizontal tokens={stackTokens}>
                        <TextField placeholder="Oddział NFZ" value={entityName} onChange={handler((m, v) => m.entityName = v)} />
                        <TextField placeholder="kod świadczeniodawcy" value={entityCode} onChange={handler((m, v) => m.entityCode = v)} />
                        <TextField style={extendedWidth1} placeholder="Użytkownik" value={username} onChange={handler((m, v) => m.username = v)} />
                        <TextField style={extendedWidth1} placeholder="Hasło" value={password} onChange={handler((m, v) => m.password = v)} />
                        <DefaultButton text="Usuń" onClick={() => props.onRemove(props.model.localKey)}  />
                    </Stack>
                    <Stack horizontal tokens={stackTokens}>
                        <TextField value={otpDesc} disabled/>
                        <TextField value={expiresDesc} disabled/>
                        <TextField style={extendedWidth2} placeholder="TOTP secret" value={otpSecret} onChange={handler((m, v) => m.otpSecret = v)} />
                        <TextField style={extendedWidth1} multiline placeholder="TOTP recovery keys" value={otpRecoveryKeys} onChange={handler((m, v) => m.otpRecoveryKeys = v)} />
                    </Stack>
                    <Stack horizontal tokens={stackTokens}>
                        <TextField value={props.changedBy} disabled defaultValue="-" />
                        <TextField value={props.changedWhen} disabled defaultValue="-"/>
                    </Stack>
                </Stack>
            </div>
        </div>
    );
}


