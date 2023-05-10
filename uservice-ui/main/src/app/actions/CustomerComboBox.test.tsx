// https://www.robinwieruch.de/react-testing-library/
import { QueryResult } from '@apollo/client';
import { render } from '@testing-library/react';
import { useListCustomersQuery } from '../../Components/.generated/components';
import { CustomerComboBox } from './CustomerComboBox';

describe('CustomerComboBox', () => {

  test('renders', () => {
    const items = {
      data: {
        Customers: {
          list: []
        }
      }
    }
    const dataResponse: typeof useListCustomersQuery = baseOptions => { return items as QueryResult };
    render(<CustomerComboBox projectId='my id' customerId='my customerId' onSelected={id => { }} useListCustomersQuery={dataResponse} />);
  });
});