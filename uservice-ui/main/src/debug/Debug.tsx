import { useIsAuthenticated, useMsal } from "@azure/msal-react";
import { DefaultButton, Stack } from "@fluentui/react";
import React from "react";
import packageJson from '../../package.json';


interface Props {
}

export const Debug: React.FC<Props> = () => {
  const stackTokens = { childrenGap: 8 }

  const a = useMsal();
  const isAuthenticated = useIsAuthenticated();

  return (
    <Stack tokens={stackTokens}>
      <Stack.Item>
        <p><small>You are running this application in <b>{process.env.NODE_ENV}</b> mode.</small></p>
      </Stack.Item>
      <Stack.Item>
        {isAuthenticated && <DefaultButton onClick={() => a.instance.logout()}>Logout</DefaultButton>}
      </Stack.Item>
      <Stack.Item>
        <p>version: {packageJson.version}</p>
      </Stack.Item>
    </Stack >
  );
};





