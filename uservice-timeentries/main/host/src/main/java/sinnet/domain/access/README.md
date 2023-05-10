# Permissions

In zero-trust world we should not apply any data read / data modification operation without checking, if such person is permitted to execute such operation. The issue is the rule of checking permission is fully customisable per application.

Are we forced to use each time custom logic without any repeatable approach to make such checking well - organized?? Lets discusss.

Our target is to define concepts and flow which should be generic enough to be used in multiple systems
Concepts:
- **User Token** - used to identify a user
- **Action** - the smallest granurality of available operation. Set of actions define a role. Some actions may be used multiple times in many roles. Examples: *Define new customer*, *Approve Invoice* etc.
- **Roles** - list of separated roles available in the system. Some of them may be global and hence they are contextless (like global admin), some of them may be created in context of some resources - we may take decision of assuming Operatior role for a user only if we have also context of legal entities where such user would like to have 'Write' action available, and 'Write' action can't be ofcourse assumed without having contect of legal eneies where such action may be actioned.
- Role Verifier - Piece of logic to define input as list of data / functions required to check single role, and producting optional result as decision of single role. *For instance, it may be a method called **roleAdminVerifier** which may take tenantId as argument and produce Optional<Role>*,
- Single Role Solver - collects all of the Role Verifiers and is responsible to invoke them in required order - the most often it is the most powerful role from available roles. The class allows us understanding that users with multiple roles is not the best idea as some roles may be working in different way
