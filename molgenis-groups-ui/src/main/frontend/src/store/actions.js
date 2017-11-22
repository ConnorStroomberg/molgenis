// @flow
import type { State, Repository } from '../flow.types'
import { SET_REPOSITORIES, SET_ERROR, ADD_REPOSITORY } from './mutations'
// $FlowFixMe
import api from '@molgenis/molgenis-api-client'

export const GET_REPOSITORY_BY_USER = '__GET_ENTITIES_IN_PACKAGE__'
export const DELETE_GROUP = '__DELETE_GROUP__'
export const CREATE_GROUP = '__CREATE_GROUP__'

const rootGroup = (group) => !!group.parent === false

function toRepository (response: any) : Repository {
  return {
    id: response.id,
    label: response.label,
    description: response.description ? response.description : '',
    rootFolderId: response.group_package
  }
}

export default {
  [GET_REPOSITORY_BY_USER] ({commit}: { commit: Function }) {
    api.get('/api/v2/sys_sec_Group').then(response => {
      console.log(response)
      commit(SET_REPOSITORIES, response.items
        .filter(rootGroup)
        .map(toRepository)
      )
    }, error => {
      commit(SET_ERROR, error)
    })
  },
  [DELETE_GROUP] ({commit, dispatch}: { commit: Function, dispatch: Function }, groupId: string) {
    api.delete_('/group/' + groupId).then(() => {
      dispatch(GET_REPOSITORY_BY_USER)
    }, error => {
      commit(SET_ERROR, 'Could not delete group.' + error)
    })

    // commit(SET_REPOSITORIES, state.repositories.filter(repository => repository.id !== groupId))
  },
  [CREATE_GROUP] ({commit, state}: { commit: Function, state: State }, formData: any) {
    api.post('/group/?label=' + formData.label).then(response => {
      commit(ADD_REPOSITORY, {
        id: response.id,
        label: response.label,
        description: '',
        rootFolderId: ''
      })
    }, error => {
      commit(SET_ERROR, 'Could not create group.' + error)
    })
  }
}
