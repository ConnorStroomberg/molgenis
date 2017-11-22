// @flow

export type Repository = {
  id: string,
  label: string,
  description: string,
  rootFolderId: string
}

export type User = {
  id: string,
  label: string
}

export type State = {
  error: ?string,
  repositories: Array<Repository>,
  groupOwnerOptions: Array<User>
}
