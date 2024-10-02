package br.com.unip.gamerating.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "review")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = 0L;
	
	@NotBlank
	@Column(name = "title")
	private String title;
	
	@NotBlank
	@Column(name = "content")
	private String content;
	
	@NotNull
//	@Size(min = 1, max = 5, message = "O valor não pode ser menor que 1 ou maior que 5")
	@Column(name = "rating")
	private Integer rating;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "platform", nullable = false)
	private Platform platform;
	
	@NotNull
	@Column(name = "date")
	private LocalDateTime date;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_username", nullable = false)
	private User user;

	public Review() {

	}
	
	public Review(Long id, @NotBlank String title, @NotBlank String content,
			@NotNull @Size(min = 1, max = 5, message = "O valor não pode ser menor que 1 ou maior que 5") Integer rating,
			@NotNull Platform platform, @NotNull LocalDateTime date, @NotNull User user) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.rating = rating;
		this.platform = platform;
		this.date = date;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}