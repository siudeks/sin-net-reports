import _ from "lodash"
import React, { useRef, useState } from "react"
import { useListCustomersQuery } from "../../Components/.generated/components"
import { FilteredComboBox } from "./FilteredComboBox"

export interface CustomerComboBoxProps {
  projectId: string,
  customerId: string | undefined,
  onSelected: (id?: string) => void,
  useListCustomersQuery: typeof useListCustomersQuery
}


/**
 * Allows to find customer based on part of name of value from secret 'Portal świadczeniodawcy'
 * @param props 
 * @returns 
 */
export const CustomerComboBox: React.FC<CustomerComboBoxProps> = props => {

  type OptionType = { key: string, text: string }
  const { useListCustomersQuery: listCustomers } = props;

  const [filteredCustomers, setFilteredCustomers] = useState<OptionType[]>([])


  const { data } = listCustomers({
    variables: {
        projectId: props.projectId
    }
})
  const items = data?.Customers.list;

  const filteredElements = (partName: string) => {
    return _.chain(items)
      .map(it => ({ key: it.id.entityId, text: it.data.customerName, secretsEx: it.secretsEx }))
      .filter(it => {
        if (!partName) return true;

        var customerName = it.text;
        if (customerName.toUpperCase().indexOf(partName.toUpperCase()) !== -1) return true;

        const specialAuth = _.chain(it.secretsEx).filter(o => o.location === 'Portal świadczeniodawcy').first().value();
        if (specialAuth && specialAuth.entityCode?.toUpperCase().indexOf(partName.toUpperCase()) !== -1) return true;

        return false;
      })
      .map(it => it as OptionType)
      .value()
  }


  const renderAfterLostLoad = useRef(0);
  if (renderAfterLostLoad.current === 0 && data) {
    renderAfterLostLoad.current = renderAfterLostLoad.current + 1;
    if (renderAfterLostLoad.current === 1) {
      setFilteredCustomers(filteredElements(''));
    }
  }
  if (renderAfterLostLoad.current !== 0 && !data) {
    renderAfterLostLoad.current = 0;
  }


  const onPendingValueChanged = (value: string) => {
    // we have rerender issue, so let change value only of different from current value
    const filtered = filteredElements(value);
    if (_.isEqual(filtered, filteredCustomers)) return;

    setFilteredCustomers(filtered);
  }



  return <FilteredComboBox label="Wybór klienta"
    selectedKey={props.customerId}
    items={filteredCustomers}
    onChange={props.onSelected}
    onPendingValueChanged={onPendingValueChanged} />
}

