package app.fitbuddy.operation.service;

import app.fitbuddy.dto.ExerciseDto;
import app.fitbuddy.jpa.entity.DefaultExercise;
import app.fitbuddy.jpa.repository.DefaultExerciseCrudRepository;
import app.fitbuddy.jpa.service.crud.ExerciseCrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewUserServiceTest {
	private final static int DUMMY_USER_ID = 17;
	@InjectMocks	NewUserService newUserService;
	@Mock	DefaultExerciseCrudRepository defaultExerciseCrudRepository;
	@Mock	ExerciseCrudService exerciseCrudService;

	@BeforeEach
	public void setUp() {
		when(defaultExerciseCrudRepository.findAll()).thenReturn(dummyDefaultExercises());
	}

	@Test
	public void newUser_whenAddsDefaultExercises_shouldFindAllDefaultExercises() {
		newUserService.addDefaultExercises(DUMMY_USER_ID);

		verify(defaultExerciseCrudRepository).findAll();
	}

	@Test
	public void newUser_whenAddsDefaultExercises_shouldCreateDtoForEachDefaultExercise() {
		newUserService.addDefaultExercises(DUMMY_USER_ID);

		verify(exerciseCrudService, times(dummyDefaultExercises().size())).create(any());
	}

	@Test
	public void newUser_whenAddsDefaultExercises_shouldCreateCorrespondingExerciseDtoFromDefaultExercises() {
		ArgumentCaptor<ExerciseDto> exerciseDtoCaptor = ArgumentCaptor.forClass(ExerciseDto.class);

		newUserService.addDefaultExercises(DUMMY_USER_ID);

		verify(exerciseCrudService, times(dummyDefaultExercises().size())).create(exerciseDtoCaptor.capture());
		List<ExerciseDto> capturedExerciseDto = exerciseDtoCaptor.getAllValues();
		dummyDefaultExercises().forEach(
				dummyDefaultExercise -> {
					assertTrue(hasCorrespondingDto(capturedExerciseDto, dummyDefaultExercise));
				}
		);
	}

	/**
	 * Checks that captured ExerciseDto list contains an exercise with expected name and appUserId DUMMY_USER_ID
	 */
	private boolean hasCorrespondingDto(List<ExerciseDto> capturedExerciseDto,
										DefaultExercise defaultExercise) {
		return capturedExerciseDto.stream()
				.anyMatch(dto -> {
					return dto.getName().equals(defaultExercise.getName()) &&
							dto.getAppUserId().equals(DUMMY_USER_ID);
				});
	}

	private List<DefaultExercise> dummyDefaultExercises() {
		return List.of(
				defaultExercise(17, "walk out and squats"),
				defaultExercise(88, "plank"),
				defaultExercise(7, "push ups")
		);
	}

	private DefaultExercise defaultExercise(int id, String name) {
		DefaultExercise defaultExercise = new DefaultExercise();
		defaultExercise.setId(id);
		defaultExercise.setName(name);
		return defaultExercise;
	}
}
