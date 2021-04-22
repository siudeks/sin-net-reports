import React from "react";
import ReactDOM from "react-dom";
import { ThemeProvider } from "@fluentui/react";
import * as serviceWorker from "./serviceWorker";

import '@fluentui/react/dist/css/fabric.css';

import { initializeIcons } from "@fluentui/react/lib/Icons";
import { Provider } from "react-redux";
import { store } from "./store/store";
import App from "./app/App";
//import { authModule } from "./msal/1autorun";

initializeIcons();

//const definedToRunSideEffect = authModule;

ReactDOM.render(
  <ThemeProvider>
    <Provider store={store}>
      <App />
    </Provider>
  </ThemeProvider>,
  document.getElementById("app")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
