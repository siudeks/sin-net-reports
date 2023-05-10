import { TimePeriod } from "./TimePeriod";
import moment from "moment";

describe('TimePeriod', () => {
  it('generate proper dates for February', () => {
      
      const sut = new TimePeriod(new Date(2001, 0, 18));
      expect(sut.getValue().dateFrom).toEqual({ year: 2001, month: 1, day: 1})
      expect(sut.getValue().dateTo).toEqual({ year: 2001, month: 1, day: 31})

      expect(sut.next().getValue().dateFrom).toEqual({ year: 2001, month: 2, day: 1})
      expect(sut.next().getValue().dateTo).toEqual({ year: 2001, month: 2, day: 28})

      expect(sut.prev().getValue().dateFrom).toEqual({ year: 2000, month: 12, day: 1})
      expect(sut.prev().getValue().dateTo).toEqual({ year: 2000, month: 12, day: 31})
  });
});