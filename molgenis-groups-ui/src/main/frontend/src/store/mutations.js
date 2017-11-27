// @flow
import type { State, Repository, User } from '../flow.types'
export const SET_ERROR = '__SET_ERROR__'
export const SET_SUCCESS_NOTICE = '__SET_SUCCESS_NOTICE__'
export const SET_REPOSITORIES = '__SET_REPOSITORIES__'
export const ADD_REPOSITORY = '__ADD_REPOSITORY__'
export const SET_GROUP_OWNER_OPTIONS = '__SET_GROUP_OWNER_OPTIONS__'

export default {
  [SET_ERROR] (state: State, error: any) {
    state.error = error
  },
  [SET_SUCCESS_NOTICE] (state: State, successNotice: any) {
    state.successNotice = successNotice
  },
  [SET_REPOSITORIES] (state: State, repositories: Array<Repository>) {
    state.repositories = repositories
  },
  /**
   * Temp function used to mock the creation of a new repository
   */
  [ADD_REPOSITORY] (state: State, newRepository: Repository) {
    state.repositories.push(newRepository)
  },
  [SET_GROUP_OWNER_OPTIONS] (state: State, groupOwnerOptions: Array<User>) {
    state.groupOwnerOptions = groupOwnerOptions
  }
}
