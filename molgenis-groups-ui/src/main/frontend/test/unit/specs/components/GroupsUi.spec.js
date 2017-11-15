import GroupsUi from 'components/GroupsUi.vue'

describe('GroupsUi', () => {
  describe('when created', () => {
    it('should use "groups-ui" as name', () => {
      expect(GroupsUi.name).to.equal('groups-ui')
    })
  })
})
