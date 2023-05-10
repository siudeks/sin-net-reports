import React from "react";
import ReactDOM from "react-dom";
import { ThemeProvider } from "@fluentui/react";
import * as serviceWorker from "./serviceWorker";

import '@fluentui/react/dist/css/fabric.css';

import { initializeIcons } from "@fluentui/react/lib/Icons";
import { Provider } from "react-redux";
import { store } from "./store/store";
import App from "./app/App";
import { AppInsightsContext } from "@microsoft/applicationinsights-react-js";
import { reactPlugin } from "./app/AppInsight";

import {
  AzureThemeLight,
  AzureThemeDark,
  AzureThemeHighContrastLight,
  AzureThemeHighContrastDark,
} from '@fluentui/azure-themes';

initializeIcons();

// https://github.com/microsoft/fluentui/tree/master/packages/azure-themes
const theme = AzureThemeLight; // or alternatively AzureThemeDark

ReactDOM.render(
  <ThemeProvider>
    <Provider store={store}>
      <AppInsightsContext.Provider value={reactPlugin}>
        <App />
      </AppInsightsContext.Provider>
    </Provider>
  </ThemeProvider>,
  document.getElementById("app")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
