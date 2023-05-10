import { CustomerType, ListCustomersItem, useListCustomersMapper } from "./useListCustomers";

describe('useListCustomers', () => {

  it('should extract filtering properties', () => {
    const customerId = { projectId: 'my projectId', entityId: 'my entityId', entityVersion: 101 };
    const given: CustomerType = {
      id: customerId,
      data: { customerName: 'my name' },
      secretsEx: [
        { location: 'my location1', entityCode: 'my code1' },
        { location: 'Portal świadczeniodawcy', entityCode: 'my code2' },
        { location: 'my location3', entityCode: 'my code3' }
      ]
    };
    var actual = useListCustomersMapper(given);
    var expected: ListCustomersItem = { customerId, name: 'my name', termNfzKodSwiadczeniodawcy: 'my code2' };
    expect(actual).toStrictEqual(expected);
  });
});
