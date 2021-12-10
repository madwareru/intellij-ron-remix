<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# RON Extended Support Changelog

## [Unreleased]
### Fixed
- Fixed char literal parsing. Now we support simple char literals.

## [0.2.6]
### Fixed
- Updated plugin to support new versions of IDE

## [0.2.5]
### Fixed
- Fixed incorrect parsing of binary and octal literals (an incorrect literals where considered fine 
so user experience where bad when serde refused to parse a file)

## [0.2.4] 2021-07-30
### Fixed
- Updated plugin to support new versions of IDE

## [0.2.3] 2021-04-07
### Fixed
- Updated plugin to support new versions of IDE
- Got rid of deprecated API usage

## [0.2.2] 2021-03-13
### Fixed
- Fixed incompatibility with old versions of IDEs

## [0.2.1] 2021-03-12
### Added
- New settings panel for code style
- Code formatting
- Error checkers for dictionaries and objects (key uniquenes and more)
### Fixed
- Added an ability to use enums as keys in dictionaries
- Fixed an issue with identifiers containing numeric literals not being parsed as it intended


## [0.1.7] 2021-01-22
### Fixed
- Got rid of compatibility problems

## [0.1.6] 2021-01-22
### Fixed
- Color highlighting options are now annotates demo text as expected

## [0.1.5] 2021-01-21
### Fixed
- Changed plugin name to successfully ship in the market

## [0.1.4] 2021-01-20
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- Plugin MVP implemented
