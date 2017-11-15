// @flow

export type Repository = {
  id: string,
  label: string,
  description: ?string,
  groupRootId: string,
  rootFolderId: string
}

export type State = {
  error: ?string,
  repositories: Array<Repository>
}
