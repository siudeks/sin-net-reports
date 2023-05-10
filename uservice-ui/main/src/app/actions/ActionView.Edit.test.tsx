import { configure, shallow } from 'enzyme';
import React from 'react';


import Adapter from '@wojtekmaj/enzyme-adapter-react-17';
import ActionViewEdit, { ActionViewEditLocal } from './ActionView.Edit';
import { ServiceAppModel } from '../../store/actions/ServiceModel';

configure({ adapter: new Adapter() });

describe('<ActionViewEditLocal />', () => {
  it('renders the components', () => {
    // var item: ServiceAppModel = {
    //   servicemanName: undefined,
    //   description: undefined,
    //   when: { year: 2000, month: 1, day: 2 },
    //   duration: undefined,
    //   distance: undefined,
    //   customer: undefined,
    //   projectId: '',
    //   entityId: '',
    //   entityVersion: 0
    // };
    // var cancelEdit = () => { };
    // var actionUpdated = () => { };
    // const wrapper = shallow(<ActionViewEditLocal  />);
  });
});