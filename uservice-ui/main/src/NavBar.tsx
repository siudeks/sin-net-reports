import * as React from 'react';
import { RouteComponentProps } from "react-router-dom";
import { Nav, INavLink } from "@fluentui/react";
import { routing } from './Routing';
import _ from 'lodash';
import { workMode, WORK_MODE } from './app/configuration/Configuration';

interface NavBarProps extends RouteComponentProps<any> { }

export const NavBar: React.FC<NavBarProps> = (props) => {
  return (
    <Nav
      onLinkClick={_onLinkClick}
      selectedKey="key1"
      styles={{
        root: {
          maxWidth: 208,
          height: 350,
          boxSizing: 'border-box',
          border: '1px solid #eee',
          overflowY: 'auto'
        }
      }}
      groups={[
        {
          links: links(
            link('Usługi', () => props.history.push(routing.actions)),
            link('Klienci', () => props.history.push(routing.customers), false),
            link('Debug', () => props.history.push(routing.debug)),
            link('Projekty', () => props.history.push(routing.debug), true))
          
        }
      ]}
    />
  );
};

interface INavLinkDev extends INavLink {
  /** true when the link should be visible only for developers, otherwise false. */
  devOnly: boolean;
}

const link = (uniqueName: string, onClick: () => void, devOnly: boolean = false): INavLinkDev => {
  const defaultProps = { url: '', target: '_blank' };
  return { name: uniqueName, key: uniqueName, onClick, devOnly, ...defaultProps };
}

const links = (...links: INavLinkDev[]): INavLink[] => {
  return _.chain(links).filter(it => !it.devOnly || workMode() === WORK_MODE.DEV).value();
}

function _onLinkClick(ev?: React.MouseEvent<HTMLElement>, item?: INavLink) {
  if (item && item.name === 'News') {
    alert('News link clicked');
  }
}
