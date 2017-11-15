// @flow
import type { Repository } from '../flow.types'
import { SET_REPOSITORIES } from './mutations'
// $FlowFixMe
// import api from '@molgenis/molgenis-api-client'

export const GET_REPOSITORY_BY_USER = '__GET_ENTITIES_IN_PACKAGE__'

let groups = [
  {
    label: 'BBMRI-NL staging area',
    description: `Actually unicorn affogato trust fund shaman seitan YOLO chillwave beard snackwave kinfolk.
             Pork belly brooklyn next level vice, tote bag blog master cleanse vape austin YOLO waistcoat lomo
             gochujang distillery. Cray sartorial pok pok bitters gochujang, pug synth tousled tacos asymmetrical
             kombucha salvia chambray prism occupy. Jianbing kickstarter raw denim banh mi small batch butcher
             vape narwhal selfies knausgaard. Disrupt fashion axe banjo, selvage godard humblebrag microdosing mixtape.`
  },
  { label: 'BBMRI-ERIC Directory',
    description: `Actually unicorn affogato trust fund shaman seitan YOLO chillwave beard snackwave kinfolk.
             Pork belly brooklyn next level vice, tote bag blog master cleanse vape austin YOLO waistcoat lomo
             gochujang distillery. Cray sartorial pok pok bitters gochujang, pug synth tousled tacos asymmetrical
             kombucha salvia chambray prism occupy. Jianbing kickstarter raw denim banh mi small batch butcher
             vape narwhal selfies knausgaard. Disrupt fashion axe banjo, selvage godard humblebrag microdosing mixtape.`
  },
  { label: 'BBMRI-FI Playground',
    description: `Actually unicorn affogato trust fund shaman seitan YOLO chillwave beard snackwave kinfolk.
             Pork belly brooklyn next level vice, tote bag blog master cleanse vape austin YOLO waistcoat lomo
             gochujang distillery. Cray sartorial pok pok bitters gochujang, pug synth tousled tacos asymmetrical
             kombucha salvia chambray prism occupy. Jianbing kickstarter raw denim banh mi small batch butcher
             vape narwhal selfies knausgaard. Disrupt fashion axe banjo, selvage godard humblebrag microdosing mixtape.`
  }
]

function toRepository (response: any) : Repository {
  return response
}

export default {
  [GET_REPOSITORY_BY_USER] ({commit}: { commit: Function }, packageId: string) {
    // api.get('/api/v2/sys_sec_Group').then(response => {
    //   commit(SET_REPOSITORIES, response.items.map(toRepository))
    // }, error => {
    //   commit(SET_ERROR, error)
    // })
    commit(SET_REPOSITORIES, groups.map(toRepository))
  }
}
