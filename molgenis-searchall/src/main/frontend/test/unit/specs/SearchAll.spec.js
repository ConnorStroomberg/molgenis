import SearchAll from 'components/SearchAll.vue'

describe('SearchAll', () => {
  describe('highlight', () => {
    it('should highlight the query part of the given text', () => {
      SearchAll.methods.$store = {
        state: {
          query: 'black dog'
        }
      }
      const input = 'Half-giant broken glasses large black dog Great Hall.'
      const highlightedText = SearchAll.methods.highlight(input)
      const expectedText = 'Half-giant broken glasses large <b class="search-result">black dog</b> Great Hall.'
      expect(expectedText).to.equal(highlightedText)
    })

    it('should pass though empty input without trowing an error', () => {
      const highlightedText = SearchAll.methods.highlight(undefined)
      expect('').to.equal(highlightedText)
    })
  })
})
