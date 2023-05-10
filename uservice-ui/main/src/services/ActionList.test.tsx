import { shallow, configure } from 'enzyme';
import React from 'react';
import { Content } from './ActionList';

import Adapter from '@wojtekmaj/enzyme-adapter-react-17';

configure({ adapter: new Adapter() });

describe('<Content />', () => {
  it('renders only selected days', () => {
    //const wrapper = shallow(<Content />);
    //expect(wrapper.find(Foo)).to.have.lengthOf(3);
  });

//   it('renders an `.icon-star`', () => {
//     const wrapper = shallow(<MyComponent />);
//     expect(wrapper.find('.icon-star')).to.have.lengthOf(1);
//   });

//   it('renders children when passed in', () => {
//     const wrapper = shallow((
//       <MyComponent>
//         <div className="unique" />
//       </MyComponent>
//     ));
//     expect(wrapper.contains(<div className="unique" />)).to.equal(true);
//   });

//   it('simulates click events', () => {
//     const onButtonClick = sinon.spy();
//     const wrapper = shallow(<Foo onButtonClick={onButtonClick} />);
//     wrapper.find('button').simulate('click');
//     expect(onButtonClick).to.have.property('callCount', 1);
//   });
});