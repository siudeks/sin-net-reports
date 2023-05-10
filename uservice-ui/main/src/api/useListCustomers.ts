import _ from 'lodash';
import { useState } from 'react';
import { ListCustomersQuery, useListCustomersQuery } from '../Components/.generated/components';
import { EntityId } from '../store/actions/ServiceModel';

export interface ListCustomersItem {
  customerId: EntityId,
  name: string
  termNfzKodSwiadczeniodawcy: string
}

export const useListCustomers: (projectId: string) => ListCustomersItem[] = (projectId) => {
  const { data } = useListCustomersQuery({
    variables: {
      projectId
    }
  });
  const [state, setState] = useState<ListCustomersItem[]>([]);

  if (data) {
    const result = _.chain(data?.Customers.list)
      .map(useListCustomersMapper)
      .value();
    if (result.length !== 0 && state.length === 0) {
      setState(result);
    }
  }

  return state;
}

let queryTypeVariable: ListCustomersQuery;
export type CustomerType = typeof queryTypeVariable.Customers.list[0];

export const useListCustomersMapper: (it: CustomerType) => ListCustomersItem = it => {
  const term1 = _.chain(it.secretsEx).filter(o => o.location === 'Portal świadczeniodawcy').map(o => o.entityCode).first().value();
  return { customerId: it.id, name: it.data.customerName, termNfzKodSwiadczeniodawcy: term1 };
}

