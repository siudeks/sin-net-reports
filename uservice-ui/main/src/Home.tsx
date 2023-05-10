import { Stack } from "@fluentui/react";
import React from "react";
import packageJson from '../package.json';


export const Home: React.FC<{}> = (props) => {
    const stackTokens = { childrenGap: 8 }

    return (
        <Stack tokens={stackTokens}>
            <Stack.Item>
                <h1>Witaj w systemie ewidencji usług.</h1>
            </Stack.Item>
            <Stack.Item>
                <p>version: {packageJson.version}</p>
            </Stack.Item>
        </Stack >
    )
}
