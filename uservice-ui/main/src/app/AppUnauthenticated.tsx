import React from "react";
import { Label, PrimaryButton, Stack } from "@fluentui/react";
import packageJson from '../../package.json';

export const View: React.FC<{ login: () => void }> = ({ login }) => {
  const stackTokens = { childrenGap: 8 }
  return (
    <Stack tokens={stackTokens}>
      <Stack.Item align="center">
        <PrimaryButton onClick={login}>Zaloguj się do aplikacji ...</PrimaryButton>
        <Label>{packageJson.version}</Label>
      </Stack.Item>
    </Stack>
  );
};
