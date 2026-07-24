# Contributing

Thank you for your interest in contributing to Atlanta Banking.

## Getting Started

1. Fork the repository.
2. Create a feature branch.

```bash
git checkout -b feature/your-feature
```

3. Make your changes.
4. Run the application and verify all tests pass.
5. Commit using meaningful commit messages.
6. Push your branch.
7. Open a Pull Request.

---

## Coding Guidelines

- Follow standard Java naming conventions.
- Keep controllers lightweight and delegate business logic to services.
- Use constructor injection.
- Write clear, self-explanatory code.
- Add validation where appropriate.
- Document new REST endpoints using OpenAPI annotations.

---

## Commit Message Examples

```text
feat(identity): add password change endpoint

fix(auth): validate expired JWT

refactor(employee): simplify mapper

test(auth): add login integration tests
```

---

## Pull Request Checklist

Before submitting a Pull Request, please ensure:

- Application builds successfully.
- Existing functionality is not broken.
- New endpoints are documented.
- Code follows the existing project structure.
- Tests are added or updated where applicable.

Thank you for contributing!