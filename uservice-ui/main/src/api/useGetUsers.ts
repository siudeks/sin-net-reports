import _ from 'lodash';
import { useState } from 'react';
import { useGetUsersQuery } from '../Components/.generated/components';

export const useGetUsers = (projectId: string) => {
  const { data } = useGetUsersQuery({
    variables: {
      projectId
    }
  });
  const [users, setUsers] = useState<string[]>([]);

  if (data) {
    const result = _.chain(data?.Users?.search).map(it => it.email).value();
    if (result.length !== 0 && users.length === 0) {
      setUsers(result);
    } 
  }

  return users;
}