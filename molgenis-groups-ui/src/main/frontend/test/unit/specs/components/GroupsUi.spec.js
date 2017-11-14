import GroupsUi from 'components/GroupsUi.vue'

describe('Groups UI', () => {
  describe('when created', () => {
    it('should use "Groups UI" as name', () => {
      expect(GroupsUi.name).to.equal('Groups UI')
    })
  })
})
