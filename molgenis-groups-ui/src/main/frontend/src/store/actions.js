// @flow
import type { State, Repository } from '../flow.types'
import { SET_REPOSITORIES, SET_ERROR, ADD_REPOSITORY } from './mutations'
// $FlowFixMe
import api from '@molgenis/molgenis-api-client'

export const GET_REPOSITORY_BY_USER = '__GET_ENTITIES_IN_PACKAGE__'
export const DELETE_GROUP = '__DELETE_GROUP__'
export const CREATE_GROUP = '__CREATE_GROUP__'

// let groups = [
//   {
//     id: 'abc1efg',
//     label: 'BBMRI-NL staging area',
//     description: `Actually unicorn affogato trust fund shaman seitan YOLO chillwave beard snackwave kinfolk.
//              Pork belly brooklyn next level vice, tote bag blog master cleanse vape austin YOLO waistcoat lomo
//              gochujang distillery. Cray sartorial pok pok bitters gochujang, pug synth tousled tacos asymmetrical
//              kombucha salvia chambray prism occupy. Jianbing kickstarter raw denim banh mi small batch butcher
//              vape narwhal selfies knausgaard. Disrupt fashion axe banjo, selvage godard humblebrag microdosing mixtape.`,
//     groupRootId: 'group1',
//     rootFolderId: ''
//   },
//   {
//     id: 'hij2kln',
//     label: 'BBMRI-ERIC Directory',
//     description: `Actually unicorn affogato trust fund shaman seitan YOLO chillwave beard snackwave kinfolk.
//              Pork belly brooklyn next level vice, tote bag blog master cleanse vape austin YOLO waistcoat lomo
//              gochujang distillery. Cray sartorial pok pok bitters gochujang, pug synth tousled tacos asymmetrical
//              kombucha salvia chambray prism occupy. Jianbing kickstarter raw denim banh mi small batch butcher
//              vape narwhal selfies knausgaard. Disrupt fashion axe banjo, selvage godard humblebrag microdosing mixtape.`,
//     groupRootId: 'group2',
//     rootFolderId: ''
//   },
//   {
//     id: 'foo3bar',
//     label: 'BBMRI-FI Playground',
//     description: `Actually unicorn affogato trust fund shaman seitan YOLO chillwave beard snackwave kinfolk.
//              Pork belly brooklyn next level vice, tote bag blog master cleanse vape austin YOLO waistcoat lomo
//              gochujang distillery. Cray sartorial pok pok bitters gochujang, pug synth tousled tacos asymmetrical
//              kombucha salvia chambray prism occupy. Jianbing kickstarter raw denim banh mi small batch butcher
//              vape narwhal selfies knausgaard. Disrupt fashion axe banjo, selvage godard humblebrag microdosing mixtape.`,
//     groupRootId: 'group3',
//     rootFolderId: ''
//   }
// ]

const rootGroup = (group) => !!group.parent === false

function toRepository (response: any) : Repository {
  return response
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
  [DELETE_GROUP] ({commit, state}: { commit: Function, state: State }, groupId: string) {
    // api.delete('/api/v2/sys_sec_Group/' + groupId).then(response => {
    //   commit(SET_REPOSITORIES, state.repositories.filter(repository => repository.id !== groupId))
    // }, error => {
    //   commit(SET_ERROR, 'Could not delete group.' + error)
    // })

    commit(SET_REPOSITORIES, state.repositories.filter(repository => repository.id !== groupId))
  },
  [CREATE_GROUP] ({commit, state}: { commit: Function, state: State }, formData: any) {
    api.post('/group/?label=' + formData.label).then(response => {
      commit(ADD_REPOSITORY, {
        id: response.id,
        label: response.label,
        description: '',
        groupRootId: '',
        rootFolderId: ''
      })
    }, error => {
      commit(SET_ERROR, 'Could not create group.' + error)
    })
  }
}
