import { DetailsList, TextField } from '@fluentui/react';
import Adapter from '@wojtekmaj/enzyme-adapter-react-17';
import { configure, shallow } from 'enzyme';
import { ListCustomersItem } from '../../api/useListCustomers';
import { EntityId } from '../../store/actions/ServiceModel';
import { CustomersView } from './Customers';


configure({ adapter: new Adapter() });

describe('<Customers />', () => {

  const createItem: () => ListCustomersItem = () => {
    const customerId: EntityId = {
      projectId: 'any',
      entityId: 'any',
      entityVersion: 42,
    }
    const rand = Math.random().toString().substr(2, 8);
    const result: ListCustomersItem = { name: 'any-' + rand, customerId, termNfzKodSwiadczeniodawcy: 'term1-' + rand };
    return result;
  }

  it('exposes provided list', () => {
    const item1 = createItem();
    const wrapper = shallow(<CustomersView
      givenProjectId="my project id"
      onNewClientCommand={() => { }}
      listCustomers={requestedProjectId => {
        expect(requestedProjectId).toEqual(requestedProjectId);
        return [item1];
      }} />);
    var details = wrapper.find(DetailsList).props();
    expect(details.items.length).toEqual(1);
  });

  it('filters by name - no matching elements', () => {
    const item1 = createItem();
    const item2 = createItem();
    const wrapper = shallow(<CustomersView
      givenProjectId="my project id"
      onNewClientCommand={() => { }}
      listCustomers={projectId => [item1, item2]} />);

    const event = {
      target: {
        value: "This is just for test"
      }
    };
    var input = wrapper.find(TextField).first();
    input.simulate("change", event);
    var details = wrapper.find(DetailsList).props();
    expect(details.items.length).toEqual(0);
  });

  it('filters by name - some matching elements', () => {
    const item1 = createItem();
    const item2 = createItem();
    const wrapper = shallow(<CustomersView
      givenProjectId="my project id"
      onNewClientCommand={() => { }}
      listCustomers={projectId => [item1, item2]} />);

    const event = {
      target: {
        value: item2.name
      }
    };
    var input = wrapper.find(TextField).first();
    input.simulate("change", event);
    var details = wrapper.find(DetailsList).props();
    expect(details.items.length).toEqual(1);
  });

  /** https://github.com/onlexnet/sin-net-reports/issues/58 */
  it('filters by prop: portal świadczeniodawcy/kod świadczeniodawcy', () => {
    const item1 = createItem();
    const wrapper = shallow(<CustomersView
      givenProjectId="my project id"
      onNewClientCommand={() => { }}
      listCustomers={projectId => [item1]} />);

    const event = {
      target: {
        value: "term1" // well known part of tested code
      }
    };
    var input = wrapper.find(TextField).first();
    input.simulate("change", event);
    var details = wrapper.find(DetailsList).props();
    expect(details.items.length).toEqual(1);
  });

});