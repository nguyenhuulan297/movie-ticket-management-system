import { api } from "./client";
import type { Movie, Seat, Showtime } from "../types";

export async function fetchShowingMovies(): Promise<Movie[]> {
  const { data } = await api.get<Movie[]>("/movies");
  return data;
}

export async function fetchMovieById(movieId: string): Promise<Movie> {
  const { data } = await api.get<Movie>(`/movies/${movieId}`);
  return data;
}

export async function searchMovies(title: string): Promise<Movie[]> {
  const { data } = await api.get<Movie[]>("/movies/search", { params: { title } });
  return data;
}

export async function fetchShowtimesByMovie(movieId: string): Promise<Showtime[]> {
  const { data } = await api.get<Showtime[]>("/showtimes", { params: { movieId } });
  return data;
}

export async function fetchShowtimeById(showtimeId: string): Promise<Showtime> {
  const { data } = await api.get<Showtime>(`/showtimes/${showtimeId}`);
  return data;
}

export async function fetchSeatsByShowtime(showtimeId: string): Promise<Seat[]> {
  const { data } = await api.get<Seat[]>("/seats", { params: { showtimeId } });
  return data;
}
