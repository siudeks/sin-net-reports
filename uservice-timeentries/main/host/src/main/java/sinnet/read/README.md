Read part of the application is created as set of async interfaces with read-only models

Alternative approach is to use request-reply approach to retrieve data, but - in my practice - it produces too much redundant objects for reading, so - in opposite to write part where commands and bus are crucial - read part is based on ready for use interfaces.
